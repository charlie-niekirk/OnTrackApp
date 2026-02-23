package me.cniekirk.ontrackapp.feature.pinned

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.cniekirk.ontrackapp.core.domain.model.pinned.PinnedService
import me.cniekirk.ontrackapp.core.domain.repository.PinnedServicesRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@Inject
@ViewModelKey(PinnedViewModel::class)
@ContributesIntoMap(scope = AppScope::class, binding = binding<ViewModel>())
class PinnedViewModel(
    private val pinnedServicesRepository: PinnedServicesRepository
) : ViewModel(), ContainerHost<PinnedState, PinnedEffect> {

    override val container = container<PinnedState, PinnedEffect>(PinnedState.Loading) {
        fetchPinnedServices()
    }

    fun serviceSelected(pinnedService: PinnedService) = intent {
        postSideEffect(PinnedEffect.NavigateToServiceDetails(pinnedService))
    }

    private fun fetchPinnedServices() = intent {
        pinnedServicesRepository.pinnedServices
            .catch { throwable ->
                reduce {
                    PinnedState.Error(throwable.message ?: "Unknown error")
                }
            }
            .collect { pinnedServices ->
                reduce {
                    PinnedState.Ready(pinnedServices = pinnedServices)
                }
            }
    }
}
